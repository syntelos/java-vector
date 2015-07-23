# [ContainerScrollPosition](OperatorsContainerScrollPosition.md) : [AbstractComponent](OperatorsAbstractComponent.md) #

> _Subclasses (none)_

## init : [super](OperatorsAbstractComponent#init.md) ##

```java

88	    public void init(){
89	        super.init();
90
91	        this.scale = 1.0f;
92	        this.position = 0.0f;
93	    }
```

## destroy : [super](OperatorsAbstractComponent#destroy.md) ##

```java

95	    public void destroy(){
96	        super.destroy();
97
98	        this.axis = null;
99	        this.boundsViewport = null;
100	        this.boundsContent = null;
101	    }
```

## resized : [super](OperatorsAbstractComponent#resized.md) ##

```java

103	    public void resized(){
104	        super.resized();
105
106	        this.layout();
107	    }
```

## modified : [super](OperatorsAbstractComponent#modified.md) ##

```java

109	    public void modified(){
110	        super.modified();
111
112	        this.layout();
113	    }
```

## layout ##

```java

122	    public void layout(Order order){
123	        switch(order){
124	        case Parent:
125	            this.modified();
126	            return;
127	        case Content:
128	            throw new UnsupportedOperationException();
129	        default:
130	            throw new IllegalStateException(order.name());
131	        }
132	    }
```

## layout ##

```java

351	    protected void layout(){
352
353	        this.boundsViewport = this.getParentVector().getBoundsVector();
354	    }
```
