(ns catfood.core
  (:import clojure.lang.IFn
           java.util.concurrent.TimeUnit
           catfood.threading.AsyncTask))

(defrecord Task
  [^IFn bg-fn
   ^IFn pre-fn
   ^IFn post-fn
   ^IFn progress-fn
   ^IFn cancel-fn
   ^AsyncTask real-task])

(defn new-task
  "Creates a new asynchronous task that will execute the given function in the background."
  [f]
  (Task. f nil nil nil nil nil))

(defn with-pre-execute
  [task f]
  (assoc task :pre-fn f))

(defn with-post-execute
  [task f]
  (assoc task :post-fn f))

(defn with-on-progress-update
  [task f]
  (assoc task :progress-fn f))

(defn with-on-cancelled
  [task f]
  (assoc task :cancel-fn f))

(def ^{:private true
       :dynamic true}
  *async-task*)

(defn publish-progress
  [& values]
  {:pre [(bound? #'*async-task*)]}
  (.superPublishProgress ^AsyncTask *async-task* (to-array values)))

(defn execute!
  ""
  ([^Task task & params]
   (let [real-task (proxy [AsyncTask] []
                     (doInBackground [_]
                       (binding [*async-task* this]
                         (let [bg-fn (.bg_fn task)]
                           (if (= [::no-args] params)
                             (bg-fn)
                             (apply bg-fn params)))))

                     (onPreExecute []
                       (when-let [pre-fn (.pre_fn task)]
                         (pre-fn)))

                     (onPostExecute [result]
                       (when-let [post-fn (.post_fn task)]
                         (post-fn result)))

                     (onProgressUpdate [values]
                       (when-let [progress-fn (.progress_fn task)]
                         (apply progress-fn values)))

                     (onCancelled []
                       (when-let [cancel-fn (.cancel_fn task)]
                         (cancel-fn))))]
     (assoc task :real-task (.execute real-task nil))))
  ([task]
   (execute! task ::no-args)))

