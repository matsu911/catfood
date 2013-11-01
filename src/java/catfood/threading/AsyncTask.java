package catfood.threading;

public abstract class AsyncTask<Params,Progress,Result> extends android.os.AsyncTask<Params,Progress,Result> {
    public final void superPublishProgress(Progress... values) {
        super.publishProgress(values);
    }
}
