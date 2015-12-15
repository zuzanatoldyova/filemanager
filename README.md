**Publication date:** 12. week of the semester 

**Submission deadline:** 2. week of the examination period

Homework assignment no. 3, FileManager
====================================


## General information

You maybe find yourself throwing bunch of documents into certain folder.
This folder usually gets messy by the time.  
Folders like `Downloads` or `Desktop`, sounds familiar? :)  
That's why you need to create program to clear this folder and move files to meaningful places.

&nbsp;

You will create simple "batch" filemanager program.  
It will parse "job file" with "cleanup rules" for given folder. 
Examples of rules:   

1. delete all tmp files (file ending `*.tmp`)
2. copy all word documents (file ending `*.doc`) to `/home/user/school_data/docs` 
3. move all excel documents (file ending `*.xls`) to `/home/user/work/spreadshets`


Your program also has to generate log file with executed file operations.

&nbsp;

The maximum number of points for this assignment is **110** _(11 points to IS)_.

- **65 points** for passing tests (attached tests do not guarantee a 100% correctenss).
- **35 points** for correct implementation (evaluated by your class teacher).
- **10 points** for clean and elegant implementation (code conventions, minimal code repetition).

In cases **when provided tests do not pass** with submitted solution you can be granted no more than **65 points** (this means that you can be granted **0 points** as well)!

### Project structure
The structure of project provided as a base for your implementation should meet the following criteria.

#### folder `src`
1. Package ```cz.muni.fi.pb162.hw03``` contains classes and interfaces provided as part of the assignment.
  - **Do not modify or add any classes or subpackages into this package.**
2. Package  ```cz.muni.fi.pb162.hw03.impl``` should contain your implementation.
  - **Anything outside this package will be ignored during evaluation.**

 
#### folder `test_data`
**Do not modify existing files in this folder..**

Folder `test_data` contains set of files and folders used for testing.
You can find job files in folder `test_data/jobs`.

```
|-- test_data
|---|-- cp_cmd
|---|-- ...
|---|-- jobs
|---|---|--- *.cmd
```

#### folder `test`
1. Package `cz.muni.fi.pb162.hw03.impl` contains set of JUnit tests.
1. Package `cz.muni.fi.pb162.hw03.impl.support` contains support classes and methods used in tests.

_You can modify tests if you want, but keep in mind, that they are here to help you. 
Deleting them or make them always pass won't help you._


## How to start implementing this homework

### Step 1: make it compile
There are some classes you need to create.

Check package `cz.muni.fi.pb162.hw03`. There is interface `FileManager`. 
You must create class `cz.muni.fi.pb162.hw03.impl.FileManagerImpl` which implements this interface.  
This is entry point of your program. It takes path to job file and, where to store log file.

```java
package cz.muni.fi.pb162.hw03;
import java.nio.charset.Charset;

public interface FileManager {
	public static final Charset JOB_FILE_CHARSET = Charset.forName("UTF-8");
	void executeJob(String jobPath, String logFilePath) throws Exception;
}
```

**Have you noticed, that method `executeJob` allows you to throw Exception?**
* you can **and should** throw appropriate exceptions to describe the cause of error
* you can define your own exceptions, checked or unchecked - it's up to you




Usage of `FileManager` interface is following:
```java
FileManager fm = new FileManagerImpl();
fm.executeJob("path/to/jobfile", "path/to/logfile");
```

* every change in filesystem should be in logfile
* if you delete one file and copy of another throws exception, the delete entry must be in log

&nbsp;

### Step 2: Create executable Main class
This project is configured to produce executable jar.
Once you add Main class, you can "compile" your executable java program with maven:

```bash
# this will execute all tests
$ mvn clean package

# if you wish, you can skip tests with
$ mvn clean pacakge -DskipTests

# maven puts all outputs to folder target
$ cd target

#execute generated program
$ java -jar hw03-filemanager-1.0-SNAPSHOT.jar 

# execute generated program with parameters
$ java -jar hw03-filemanager-1.0-SNAPSHOT.jar /path/to/job_file /path/to/log_file
```

**Your main class should do following:**
1. check correct number of program arguments
2. invoke `FileManager.executeJob`
3. print success/error message on std / err out when program ends





## Job file syntax
First line of job file **(required)**
* contains path to folder which should be cleaned up.  
* is in the format `root;<path>`

Every other line - **(must be at least one)** is :
* empty line
* comment line (line starts with `#`)
* command in the form: `<command>;<file extension>;<optional argument>`.

It's also usefull and common practice, to leave some notes in configuration files,
especially when you need to modify something, you (or somebody else) wrote year ago :).
For this reason, **it should be possible to have empty lines and comments in job file**.
```bash
root;<path to existing folder>
# comment
<op>;<file extension>;<argument>

# other line
<op>;<file extension>;<argument>
...
```

## Example instructions
Check folder `test_data/jobs/` for job files used in tests.
```bash
root;C:\Users\jludvice\Downloads
MV;jpg;C:\Users\jludvice\Pictures\are\awesome

# theese are just temp files, can be safely deleted
DEL;tmp 

# all libre office documents in this folder are related to my thesis
CP;odt;C:\Users\jludvice\Documents\bachelor_thesis
```

This is example of job file which will

* move all `*.jpg` pictures from `Downloads` to `C:\Users\jludvice\Pictures\are\awesome` (create target folder if doesn't exist)
* delete all `*.tmp` files form `C:\Users\jludvice\Downloads`
* copy all `*.odt` documents from `Downloads` to `C:\Users\jludvice\Documents\bachelor_thesis`.



Remember that **all commands must work recursivelly!**  
For example if you have `Downloads\somebody.jpg` and `Downloads\older mess\nobody.jpg`,  
both pictures will end up in `C:\Users\Pictures\are\awesome`.

```
# source in downloads
|-- C:\Users\jludvice\Downloads\
|---|-- somebody.jpg
|---|-- older mess
|---|---|--- nobody.jpg

# target in Pictures\are\awesome
|-- C:\Users\jludvice\Pictures\are\awesome
|---|-- somebody.jpg
|---|-- nobody.jpg
```

# FAQ

## Recursive filesystem operations
How to find files recursively in given folder?

> There are few approaches described in [this snippet](https://gitlab.com/snippets/11504).
 

## Log file
How to do logging in Java? It wasn't explained in the course.

> Yes, proper logging in java wasn't covered in this course.   
You should just create file on given path and write line for each executed operation.  
Expected format is `operation;original file;new file`. For example:  
```
DEL;work/src/job1/a.png
MV;work/src/job1/sub/folder/jpg.gif;work/dest/pictures/jpg.gif
CP;work/src/job1/b.jpg;work/dest/pictures/b.jpg
```

## Absolute vs relative file path
Should I use relative or absolute paths?
> Provided tests generate job files with absolute paths.  
Your program should handle both relative and absolute paths. It usually works out of the box. 

What about logfile I generate?
>Your generated logfile may contain absolute or relative paths. Choice is up to you. Both are ok.  
However be consistent. If you choose to write absolute paths to log, then always write abslute path and vice versa.

## Multiple files with same name
What if there are multiple files with same file name?  
For instance this, in `root` folder:
```
|-- folder
|---|-- somebody.jpg
|---|-- subfolder
|---|---|--- somebody.jpg
```
>You obviously don't want to loos any data and common practice in this case is to append counter after file name.
>**Use 3-digit counter, starting from 001, as shown in following example.**
```
|-- target folder
|---|-- somebody.jpg
|---|-- somebody_001.jpg
```
>And of course proper entries in your log file:
```
CP;folder/somebody.png;target folder/somebody.png
CP;folder/subfolder/somebody.png;target folder/somebody_001.png
```

## Multiple commands matches file name
You might face the situation, where multiple commands in job file matches given file name. For instance:
```
DEL;doc
CP;doc;destdir/docs
```
>It's important to follow order of commands in job file. 
In this case all word documents are deleted and nothing is copied, 
because the files either didn't exist or were deleted.

## How tests work
This homework is about making changes on filesystem. 
While your program is executed in tests, it may read, write and delete files.  
This means, that we need fresh test data for each test execution.

**How is it done?**  
There is folder `test_data` containing sample folder structures and job files for tests.
Each test is asociated with some job file and folder structure.  

Let's assume we want to run test `SimpleCopyTest` which tests job named `cp_cmd`.  
To prepare test enviroment, it:

1. deletes `work` folder (to make sure, we start with clean enviroment)
1. copies folder `test_data/cp_cmd` to `work/src/cp_cmd`
1. prepares job file `test_data/jobs/cp_cmd.cmd` 
    * write paths in platform specific way (you should know that file paths are different on windows and linux :)
    * write modified job file into `work/jobfile.cmd`
1. once enviroment is ready, test starts by calling method `executeJob("work/jobfile.cmd", "log/path")` on your `FileManagerImpl` class 


It's implemented with feature called [Rules](https://github.com/junit-team/junit/wiki/Rules) in
[JUnit 4](http://junit.org/) test framework.  
Check `@Rule` annotation in test classes and `before()` method of `TestSupport` class if you wan't to know how  more details.

```java
package cz.muni.fi.pb162.hw03;
import cz.muni.fi.pb162.hw03.support.TestSupport;

public class SimpleCopyTest {
	@Rule
	public TestSupport testSupport = TestSupport.forJob("cp_cmd");
}
```
