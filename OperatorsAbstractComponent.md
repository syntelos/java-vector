# [AbstractComponent](OperatorsAbstractComponent.md) : Object #

> _Subclasses_
> [Border](OperatorsBorder.md)
> [BorderComponent](OperatorsBorderComponent.md)
> [Container](OperatorsContainer.md)
> [ContainerScrollPosition](OperatorsContainerScrollPosition.md)
> [Grid](OperatorsGrid.md)

## init ##

```java

55	    public void init(){
56
57	        Component parent = this.getParentVector();
58
59	        this.destroy();
60
61	        this.visible = true;
62	        this.mouseIn = false;
63
64	        this.transform.init();
65
66	        this.bounds.init();
67
68	        this.setParentVector(parent);
69	    }
```

## init ##

```java

70	    protected void init(Boolean init){
71	        if (null != init && init.booleanValue()){
72
73	            this.init();
74	        }
75	    }
```

## destroy ##

```java

81	    public void destroy(){
82
83	        this.parent = null;
84	    }
```

## resized ##

```java

89	    public void resized(){
90	    }
```

## modified ##

```java

95	    public void modified(){
96	    }
```

## relocated ##

```java

101	    public void relocated(){
102	    }
```
