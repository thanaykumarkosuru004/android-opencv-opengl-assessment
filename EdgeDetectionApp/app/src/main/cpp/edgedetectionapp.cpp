// This is example C++ code for your .cpp file
#include <jni.h>
#include <string>

// This is a JNI function that can be called from your Kotlin/Java code.
// The semicolon after jstring has been removed.
extern "C" [[maybe_unused]] JNIEXPORT jstring  extern "C" JNICALL
Java_com_example_edgedetectionapp_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

