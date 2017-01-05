(ns ring-image-crop.core-test
  (:require [clojure.test :refer :all]
            [ring-image-crop.core :refer :all]
            [ring.middleware.params :refer (wrap-params)]
            [ring.middleware.keyword-params :refer (wrap-keyword-params)]
            [ring.middleware.file :refer (wrap-file)]
            [ring.mock.request :as mock]))

(defn handler [req] {:body "HI" :status 200})

(def app 
  (-> handler
      (wrap-file "public")
      (wrap-image-crop "public")
      (wrap-keyword-params)
      (wrap-params)))

(deftest request-checks
  (testing "request image detection"
    (is (request-is-image? (mock/request :get "/img/test.png")))
    (is (not (request-is-image? (mock/request :get "/img/test.html"))))))

(deftest pass-thru
  (let [url (str "/kitten.png?width=100&height=10" "&" (str (java.util.UUID/randomUUID)))
        req (mock/request :get url)
        res1 (app req)
        res2 (app req)]
    (is (.startsWith (str (:body res1)) "public"))
    (is (.startsWith (str (:body res2)) "/tmp"))))



