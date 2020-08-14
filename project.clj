(defproject brew-bot "2.2.0"
            :description "A quick, dirty way to get randomized beer recipes"
            :url "https://github.com/nnichols/brew-bot"
            :license {:name "Eclipse Public License"
                      :url "http://www.eclipse.org/legal/epl-v10.html"}
            :dependencies [[cljx-sampling "0.1.0"]
                           [com.wallbrew/common-beer-format "1.3.0"]
                           [metosin/spec-tools "0.10.2"]
                           [nnichols "0.10.0"]
                           [org.clojure/clojure "1.10.1"]
                           [org.clojure/clojurescript "1.10.758" :scope "provided"]]

            :plugins [[lein-cljsbuild "1.1.7"]]

            :aliases {"test-build" ["do" "clean" ["cljsbuild" "once" "test"] ["doo" "once"] ["test"]]}

            :profiles {:uberjar {:aot :all}
                       :dev {:dependencies [[doo "0.1.11"]]
                             :plugins      [[lein-doo "0.1.11"]]}}

            :min-lein-version "2.5.3"

            :cljsbuild {:builds
                        [{:id "test"
                          :source-paths ["src" "test"]
                          :compiler {:main "brew-bot.runner"
                                     :output-to "target/test/app.js"
                                     :output-dir "target/test/js/compiled/out"
                                     :optimizations :none
                                     :parallel-build true}}]}

            :doo {:build "test"
                  :alias {:default [:chrome-headless-no-sandbox]}
                  :paths {:karma "./node_modules/karma/bin/karma"}
                  :karma {:launchers {:chrome-headless-no-sandbox {:plugin "karma-chrome-launcher"
                                                                   :name   "ChromeHeadlessNoSandbox"}}
                          :config    {"captureTimeout"             210000
                                      "browserDisconnectTolerance" 3
                                      "browserDisconnectTimeout"   210000
                                      "browserNoActivityTimeout"   210000
                                      "customLaunchers"            {"ChromeHeadlessNoSandbox"
                                                                    {"base"  "ChromeHeadless"
                                                                     "flags" ["--no-sandbox" "--disable-dev-shm-usage"]}}}}})
