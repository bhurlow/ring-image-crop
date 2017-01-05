# ring-image-crop

ring middleware that auto crops and serves urls according the `width` and `height` query params

### Usage

in your project.clj:

```clj
[ring-image-crop "0.0.1"]
```

Then install the handler.
(note that the params and keyword-params are *required*)

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


