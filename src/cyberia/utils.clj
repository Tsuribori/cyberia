(ns cyberia.utils 
  (:require [cheshire.core :refer [parse-string]]
            [clojure.string :as string]))

(def DEFAULT-CONFIG-DIR "~/.cyberia")
(def DEFAULT-CRED-FILE (str DEFAULT-CONFIG-DIR "/default.json"))

(defn expand-home
  [s]
  (if (.startsWith s "~")
    (string/replace-first s "~" (System/getProperty "user.home"))
    s))

(defn cred-file
  []
  (expand-home DEFAULT-CRED-FILE))

(defn get-user
  []
  (let [file-contents (slurp (cred-file))]
    (parse-string file-contents true)))