#include <linux/fb.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <errno.h>
#include <string.h>

#include "zhengzhiren_android_hardware_Framebuffer.h"

#define DEFAULT_FB "/dev/graphics/fb0"

//// 将16位的rgb565转换为24位的rgb888
//// RRRRRGGGGGGBBBBB -> RRRRRRRRGGGGGGGGBBBBBBBB 
//void convert565to24(void *buf565, void *buf24, int xres, int yres)
//{
//	int i, j;
//	short tmp;
//
//	for(i = 0, j = 0; i < xres * yres; ++i)
//	{
//		tmp = ((short*)buf565)[i];
//		//TODO
//		((char*)buf24)[j++] = (tmp >> 11) & 0x1F;	//Red	0x1F = 11111B
//		((char*)buf24)[j++] = (tmp >> 5) & 0x3F;	//Green	0x3F = 111111B
//		((char*)buf24)[j++] = tmp & 0x1F;		//Blue	0x1F = 11111B
//	}
//}

JNIEXPORT void JNICALL Java_zhengzhiren_android_hardware_Framebuffer_getFramebuffer
  (JNIEnv *env, jclass this, jobject buf)
{
	int fd;
	int bytes_per_pixel;
	int offset;

	/*char *buf_rgb24;*/
	char *out_buf;
	jlong capacity; 
	char *fb_mem;

	struct fb_fix_screeninfo fb_finfo;
	struct fb_var_screeninfo fb_vinfo;

	//open framebuffer
	fd = open(DEFAULT_FB, O_RDONLY);
	if(fd == -1) goto fail;

	if(ioctl(fd, FBIOGET_FSCREENINFO, &fb_finfo) < 0) goto fail;
	if(ioctl(fd, FBIOGET_VSCREENINFO, &fb_vinfo) < 0) goto fail;

	//mmap
	fb_mem = mmap(NULL, fb_finfo.smem_len, PROT_READ, MAP_SHARED, fd, 0);
	if(MAP_FAILED == fb_mem) goto fail;

	out_buf = (*env)->GetDirectBufferAddress(env, buf);
	capacity = (*env)->GetDirectBufferCapacity(env, buf);

	bytes_per_pixel = fb_vinfo.bits_per_pixel >> 3;

	offset = fb_vinfo.xoffset * bytes_per_pixel;
	offset += fb_vinfo.xres * fb_vinfo.yoffset * bytes_per_pixel;

	//16位色的一般是rgb565，直接拷贝缓冲区。
	//
	//32位色的frame buffer是小端（little endian），
	//测试结果表明：每个像素的内容从高位到低位是ABGR，需要反转。
	if(16 == fb_vinfo.bits_per_pixel)
	{
		memcpy(out_buf, fb_mem + offset, capacity);
	}
	else if(32 == fb_vinfo.bits_per_pixel)	
	{
		int i;
		char *start = fb_mem + offset;
		for(i = 0; i < capacity; i += 4)
		{
			out_buf[i+3] = start[i+3];	//A
			out_buf[i+2] = start[i+0];	//R
			out_buf[i+1] = start[i+1];	//G
			out_buf[i+0] = start[i+2];	//B
		}
	}

	//清理
	close(fd);
	munmap(fb_mem, fb_finfo.smem_len);
	/*convert565to24(fb_mem, buf_rgb24, fb_vinfo.xres, fb_vinfo.yres);*/

fail:
	if(fd >= 0)
		close(fd);
}

JNIEXPORT jint JNICALL Java_zhengzhiren_android_hardware_Framebuffer_getWidth
  (JNIEnv *env, jclass this)
{
	int fd;
	struct fb_var_screeninfo fb_vinfo;

	fd = open(DEFAULT_FB, O_RDONLY);
	if(fd == -1) return -1;

	if(ioctl(fd, FBIOGET_VSCREENINFO, &fb_vinfo) < 0)
	{
		close(fd);
		return -1;
	}

	return fb_vinfo.xres;
}

JNIEXPORT jint JNICALL Java_zhengzhiren_android_hardware_Framebuffer_getHeight
  (JNIEnv *env, jclass this)
{
	int fd;
	struct fb_var_screeninfo fb_vinfo;

	fd = open(DEFAULT_FB, O_RDONLY);
	if(fd == -1) return -1;

	if(ioctl(fd, FBIOGET_VSCREENINFO, &fb_vinfo) < 0)
	{
		close(fd);
		return -1;
	}

	return fb_vinfo.yres;
}

JNIEXPORT jint JNICALL Java_zhengzhiren_android_hardware_Framebuffer_getBytesPerPixel
  (JNIEnv *env, jclass this)
{
	int fd;
	struct fb_var_screeninfo fb_vinfo;

	fd = open(DEFAULT_FB, O_RDONLY);
	if(fd == -1) return -1;

	if(ioctl(fd, FBIOGET_VSCREENINFO, &fb_vinfo) < 0)
	{
		close(fd);
		return -1;
	}

	return fb_vinfo.bits_per_pixel >> 3;
}
