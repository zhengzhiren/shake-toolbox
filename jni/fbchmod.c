/*修改framebuffer设备文件的权限，使非特权用户可读*/

#include <sys/stat.h>
#include <stdio.h>
#include <string.h>

#define DEFAULT_FB "/dev/graphics/fb0"

#define PERM_READ "read"
#define PERM_NOREAD "noread"

void usage()
{
	printf("\nUsage: fbchmod <read|noread>\n");
}

int main(int argc, char *argv[])
{
	char		*perm;	//权限（命令行参数）
	mode_t		mode;
	struct stat	fb_stat;

	if(argc != 2)
	{
		int i;
		for(i = 0; i < argc; ++i)
			printf("%s ", argv[i]);
		usage();
		return -1;
	}

	perm = argv[1];

	//获取文件信息
	if(stat(DEFAULT_FB, &fb_stat) == -1)
	{
		printf("stat failed!\n");
		return -1;
	}

	mode = fb_stat.st_mode;
	//添加读权限
	if(strcmp(perm, PERM_READ) == 0)
	{
		if((mode & S_IROTH) != S_IROTH)
		{
			mode |= S_IROTH;
			if(chmod(DEFAULT_FB, mode) == -1)
			{
				printf("chmod failed!\n");
				return -1;
			}
		}
		printf("Read permission granted.\n");
	}
	//清除读权限
	else if(strcmp(perm, PERM_NOREAD) == 0)
	{
		if((mode & S_IROTH) == S_IROTH)
		{
			mode &= ~S_IROTH;
			if(chmod(DEFAULT_FB, mode) == -1)
			{
				printf("chmod failed!\n");
				return -1;
			}
		}
		printf("Read permission revoked.\n");
	}
	else
	{
		printf("Unknown option: %s\n", perm);
		usage();
		return -1;
	}

	return 0;
}
