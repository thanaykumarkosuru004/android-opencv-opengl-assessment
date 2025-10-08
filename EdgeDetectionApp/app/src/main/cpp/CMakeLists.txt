cmakecmake_minimum_required(VERSION 3.18.1)
cmake_minimum_required(VERSION 3.18.1)
# This is the correct content for CMakeLists.txt
cmake_minimum_required(VERSION 3.18.1)


project("native-lib")

# Add your C++ source file
# Ensure the cpp file name here matches your actual file name
add_library(native-lib SHARED edgedetectionapp.cpp) # Or native-lib.cpp if that's the name

# Find Android log library for debugging
find_library(log-lib log)

# Set path to OpenCV SDK (CRITICAL: Adjust to your unzipped SDK)
# Example: Windows: "C:/opencv-4.8.0-android-sdk/sdk/native/jni"
set(OpenCV_DIR "YOUR_FULL_PATH_TO_OPENCV_SDK/sdk/native/jni")
find_package(OpenCV REQUIRED PATHS ${OpenCV_DIR})

# Link libraries: OpenCV + Android log to your native lib
target_link_libraries(native-lib ${OpenCV_LIBS} ${log-lib})

# Include OpenCV headers for #include <opencv2/...>
target_include_directories(native-lib PRIVATE ${OpenCV_INCLUDE_DIRS})
