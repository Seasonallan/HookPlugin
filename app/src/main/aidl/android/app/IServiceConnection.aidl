// IServiceConnection.aidl
package android.app;

// Declare any non-default types here with import statements

interface IServiceConnection {
    void connected(in ComponentName name, IBinder service);
}
