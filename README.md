<h1 align="center">Straighten text</h1>


# Description

StraightenText is a Java class that implements methods to straighten a BufferedImage.  
It works mainly with images with text.

# Dependencies
```markdown
<dependency>
    <groupId>com.github.simoska4.removespaces</groupId>
    <artifactId>remove-spaces</artifactId>
    <version>1.0</version>
</dependency>
<dependency>
    <groupId>com.github.simoska4.editimage</groupId>
    <artifactId>edit-image</artifactId>
    <version>1.1</version>
</dependency>
```

<br>

## Sample
We get the following Images as input and then we show the results using the methods implemented.  


#### Input Image1
![Original BufferedImage](https://github.com/simoska4/straightentext/blob/master/sample/input1.png)  

#### Output Image1
![Original BufferedImage](https://github.com/simoska4/straightentext/blob/master/sample/input1_rotated.png)  

#### Input Image2
![Original BufferedImage](https://github.com/simoska4/straightentext/blob/master/sample/input2.png)  

#### Output Image2
![Original BufferedImage](https://github.com/simoska4/straightentext/blob/master/sample/input2_rotated.png)  



<br><br>
  
## Usage

#### Apache Maven  
```markdown
<dependency>
  <groupId>com.github.simoska4.straightentext</groupId>
  <artifactId>straighten-image</artifactId>
  <version>1.0</version>
</dependency>
```


#### Gradle Groovy DSL  
```markdown
implementation 'com.github.simoska4.straightentext:straighten-image:1.0'
```


#### Gradle Kotlin DSL 
```markdown
compile("com.github.simoska4.straightentext:straighten-image:1.0")
```


#### Scala SBT 
```markdown
libraryDependencies += "com.github.simoska4.straightentext" % "straighten-image" % "1.0"
```


#### Apache Ivy
```markdown
<dependency org="com.github.simoska4.straightentext" name="straighten-image" rev="1.0" />
```


#### Groovy Grape
```markdown
@Grapes(
  @Grab(group='com.github.simoska4.straightentext', module='straighten-image', version='1.0')
)
```


#### Leiningen
```markdown
[com.github.simoska4.straightentext/straighten-image "1.0"]
```


#### Apache Buildr
```markdown
'com.github.simoska4.straightentext:straighten-image:jar:1.0'
```


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.


## License
[MIT](https://choosealicense.com/licenses/mit/)
