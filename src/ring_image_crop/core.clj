(ns ring-image-crop.core
  (:require [clojure.java.shell :refer (sh)]
            [clojure.java.io :as io]
            [ring.util.response :refer (file-response)]
            [ring.util.codec :as codec]
            [ring.util.request :as request])
  (:import java.security.MessageDigest java.math.BigInteger))

(defn md5 [s]
  (let [algorithm (MessageDigest/getInstance "MD5")
        size (* 2 (.getDigestLength algorithm))
        raw (.digest algorithm (.getBytes s))
        sig (.toString (BigInteger. 1 raw) 16)
        padding (apply str (repeat (- size (count sig)) "0"))]
    (str padding sig)))

(defn assert-gm-exists []
  (= 0 (:exit (sh "gm" "version"))))

(defn gm-crop [size path out]
  (sh "gm" "convert"
      "-resize" "500x500^"
      "-gravity" "Center"
      "-background" "black"
      "-flatten"
      "-crop" 
      (str (:width size) "x" (:height size) "+0+0") 
      "+repage" 
      path 
      out))

(defn request-is-image? [req]
  (let [uri (:uri req)
        endings #{".png" ".jpg"}]
    (some identity (map #(.endsWith uri %) endings))))

(defn request-has-crop-query? [{params :params}]
  (and (:width params) 
       (:height params)))

(defn apply-crop [req root-path]
  (let [checksum (md5 (str (:uri req) "&" (:query-string req))) 
        size (select-keys (:params req) [:width :height])
        path (str "/tmp/" checksum)
        exists? (.exists (io/as-file path))]
    (if exists?
      (file-response path)
      (let [original-path (subs (codec/url-decode (request/path-info req)) 1)]
        (gm-crop size (str root-path "/" original-path) path)
        (file-response original-path {:root root-path})))))

(defn wrap-image-crop [handler root-path]
  (assert-gm-exists)
  (fn [req]
    (if (request-is-image? req)
      (if (request-has-crop-query? req)
        (apply-crop req root-path)
        (handler req))
      (handler req))))

