import time
import os
import logging
from subprocess import run
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler

# base config
local_repo_path = 'C:\\Users\\13460\\Desktop\\ImageUp'
local_image_path = 'C:\\Users\\13460\\Desktop\\ImageUp\\Image'
local_log_path = 'D:\\Log\\sync\\sync.log'
commit_content = 'script'
prefix = 'p'
# rename
max_retries = 3
retry_delay = 3


class MyEventHandler(FileSystemEventHandler):
    def __init__(self):
        FileSystemEventHandler.__init__(self)

    def on_moved(self, event):
        if event.is_directory:
            logging.info(
                "Directory moved:{src_path} -> {dest_path}".format(src_path=event.src_path, dest_path=event.dest_path))
        else:
            logging.info(
                "File moved:{src_path} -> {dest_path}".format(src_path=event.src_path, dest_path=event.dest_path))
        commit_images_files()

    def on_created(self, event):
        if event.is_directory:
            logging.info("Directory created:{file_path}".format(file_path=event.src_path))
        else:
            rename_new_image(event.src_path, prefix)
            logging.info("File created:{file_path}".format(file_path=event.src_path))
        commit_images_files()

    def on_deleted(self, event):
        if event.is_directory:
            logging.info("Directory deleted:{file_path}".format(file_path=event.src_path))
        else:
            logging.info("File deleted:{file_path}".format(file_path=event.src_path))
        commit_images_files()

    def on_modified(self, event):
        if event.is_directory:
            logging.info("Directory modified:{file_path}".format(file_path=event.src_path))
        else:
            logging.info("File modified:{file_path}".format(file_path=event.src_path))
        commit_images_files()


def commit_images_files():
    cmd_add = 'git add .'
    cmd_commit = 'git commit -m ' + commit_content
    cmd_push = 'git push -u origin master'
    os.chdir(local_repo_path)
    run(cmd_add, shell=True)
    run(cmd_commit, shell=True)
    run(cmd_push, shell=True)


def find_max_number_in_filenames(folder_path, prefix):
    max_number = 0
    for filename in os.listdir(folder_path):
        if filename.startswith(prefix):
            try:
                number = int(filename[len(prefix):-4])
                max_number = max(max_number, number)
            except ValueError:
                pass
    return max_number


def rename_new_image(new_image_path, prefix):
    folder_path = os.path.dirname(new_image_path)
    max_number = find_max_number_in_filenames(folder_path, prefix)
    new_image_number = max_number + 1
    _, ext = os.path.splitext(new_image_path)
    new_image_name = f"{prefix}{new_image_number}{ext}"
    new_image_path_with_name = os.path.join(folder_path, new_image_name)
    for i in range(max_retries):
        try:
            os.rename(new_image_path, new_image_path_with_name)
            break
        except PermissionError as e:
            logging.warning(f"PermissionError: {e}. Retrying ({i + 1}/{max_retries})...")
            time.sleep(retry_delay)
        except  FileNotFoundError:
            time.sleep(retry_delay)
    logging.info(f"File Renamed: {new_image_path} to {new_image_path_with_name}")


def estimate_log_file(log_path):
    if not os.path.exists(log_path):
        file = open(log_path, 'w')
        file.close()


if __name__ == '__main__':
    path = local_image_path
    log_path = local_log_path
    estimate_log_file(log_path)
    logging.basicConfig(filename=log_path, level=logging.INFO, format='%(asctime)s  %(message)s')
    myEventHandler = MyEventHandler()
    observer = Observer()
    observer.schedule(myEventHandler, path, recursive=True)
    observer.start()
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        observer.stop()
    observer.join()