LOCAL_PATH := $(call my-dir)

# 编译为库文件(.so)供jni调用
include $(CLEAR_VARS)
LOCAL_MODULE    := fbread
LOCAL_SRC_FILES := fbread.c
include $(BUILD_SHARED_LIBRARY)

# 编译为可执行文件
include $(CLEAR_VARS)
LOCAL_MODULE    := fbchmod
LOCAL_SRC_FILES := fbchmod.c
include $(BUILD_EXECUTABLE)
