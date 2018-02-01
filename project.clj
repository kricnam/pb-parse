(defproject pb-parse "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.google.protobuf/protobuf-java "2.5.0"]
                 [org.flatland/protobuf "0.7.0"]]
  :main ^:skip-aot pb-parse.core
  :target-path "target/%s"
  :bin {:name "pb-parse"}
  :protoc "/usr/local/bin/protoc"
  
  :profiles {:uberjar {
                       :aot :all
                       }
             :dev {:plugins [
                             [lein-binplus "0.6.4"]
                             [lein-protobuf "0.4.0"] 
                             ]
                   
                   }
             }
  )
