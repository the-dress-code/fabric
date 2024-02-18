(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'com.github.the-dress-code/fabric)
(def version (format "1.2.%s" (b/git-count-revs nil)))
(def class-dir "target/classes")
(def uber-file (format "target/%s-%s-standalone.jar" (name lib) version))
(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn clean [_]
  (b/delete {:path "target"}))

(defn uber [_]
  (clean nil)
  (b/copy-dir {:src-dirs ["src" "db" "resources"]
               :target-dir class-dir})
  #_(b/copy-file {:src "fabric_dev.sqlite3"
                :target class-dir})
  (b/compile-clj {:basis @basis
                  :ns-compile '[server]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file uber-file
           :basis @basis
           :main 'server}))
