# [BorderComponent](OperatorsBorderComponent.md) : [AbstractComponent](OperatorsAbstractComponent.md) #

> _Subclasses_
> [Text](OperatorsText.md)

## destroy : [super](OperatorsAbstractComponent#destroy.md) ##

```java

51	    public void destroy(){
52	        super.destroy();
53
54	        Border border = this.border;
55	        if (null != border){
56	            this.border = null;
57	            border.destroy();
58	        }
59	        this.background = null;
60	        this.backgroundOver = null;
61	    }
```

## resized : [super](OperatorsAbstractComponent#resized.md) ##

```java

63	    public void resized(){
64	        super.resized();
65
66	        Border border = this.border;
67	        if (null != border){
68	            border.resized();
69	        }
70	    }
```

## modified : [super](OperatorsAbstractComponent#modified.md) ##

```java

72	    public void modified(){
73	        super.modified();
74
75	        Border border = this.border;
76	        if (null != border){
77	            border.modified();
78	        }
79	    }
```
