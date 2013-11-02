(defproject catfood "0.1.1"
  :description "Catfood is a suppliment for Neko"
  :url "http://github.com/matsu911/catfood"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure-android/clojure "1.5.1-jb"]]
  :java-source-paths ["src/java"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :target-path "target"
  :android {:library true
            :target-sdk :ics})
