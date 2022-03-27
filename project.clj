(defproject cyberia "0.1.0"
  :description "Pleroma CLI client"
  :url "http://example.com/FIXME"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [cli-matic "0.4.3"]
                 [clj-http "3.12.3"]
                 [cheshire "5.10.2"]
                 [com.github.clj-easy/graal-build-time "0.1.4"]]
  :main ^:skip-aot cyberia.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :plugins [[io.taylorwood/lein-native-image "0.3.1"]]
  :native-image {:opts ["--no-fallback"
                        "--enable-http"
                        "--enable-https"
                        "--allow-incomplete-classpath"
                        "--native-image-info"
                        "--enable-url-protocols=http,https"
                        "-H:ReflectionConfigurationFiles=resources/reflection.json"
                        "-H:+ReportExceptionStackTraces"
                        "-H:CCompilerOption=-pipe"
                        "-H:+TraceSecurityServices"
                        "-H:+StaticExecutableWithDynamicLibC"
                        "-J-Dclojure.spec.skip.macros=true"
                        "-J-Dclojure.compiler.direct-linking=true"
                        "-J-Xmx3G"
                        "--initialize-at-build-time=com.fasterxml.jackson"
                        "--initialize-at-build-time=org.apache.http.conn.ssl.AllowAllHostnameVerifier"
                        "--initialize-at-build-time=org.apache.http.conn.ssl.StrictHostnameVerifier"
                        "--initialize-at-build-time=org.apache.http.conn.ssl.SSLConnectionSocketFactory"
                        "--initialize-at-build-time=org.apache.commons.logging.LogFactory"
                        "--initialize-at-build-time=org.apache.commons.logging.impl.LogFactoryImpl"
                        "--initialize-at-build-time=org.apache.commons.logging.impl.Jdk14Logger"
                        "--initialize-at-build-time=org.apache.http.conn.ssl.BrowserCompatHostnameVerifier"
                        "--initialize-at-build-time=clj_tuple$hash_map,clj_tuple$vector"]})
