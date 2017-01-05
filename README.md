# ring-image-crop

ring middleware that auto crops and serves urls according the `width` and `height` query params

```clj
(def app
  (-> handler
      (wrap-file "public")
      (wrap-image-crop "public")
      (wrap-keyword-params)
      (wrap-params)))
```

```clj
;; first requests would crop if crop not already made
(get "/kitten.png?width=100&height=10")

;; subsequent reqs return cropped version
(get "/kitten.png?width=100&height=10")
```


