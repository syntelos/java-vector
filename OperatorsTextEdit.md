# [TextEdit](OperatorsTextEdit.md) : [Text](OperatorsText.md) #

> _Subclasses (none)_

## init : [super](OperatorsText#init.md) ##

```java

44	    public void init(){
45	        super.init();
46
47	        this.home = Home.Nil;
48
49	        this.blink = new Blink(this,800);
50
51	        if (this.string instanceof Editor)
52	            ((Editor)this.string).end();
53	        else
54	            this.string = new Editor(this.home);
55	    }
```

## destroy : [super](OperatorsText#destroy.md) ##

```java

57	    public void destroy(){
58	        super.destroy();
59
60	        this.home = null;
61	        this.blink = null;
62	    }
```
