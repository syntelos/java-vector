# [Border](OperatorsBorder.md) : [AbstractComponent](OperatorsAbstractComponent.md) #

> _Subclasses (none)_

## init : [super](OperatorsAbstractComponent#init.md) ##

```java

73	    public void init(){
74	        super.init();
75
76	        this.color = Color.black;
77	        this.style = Style.SQUARE;
78	        this.fill = false;
79	        this.fixed = false;
80	        this.arc = 0f;
81	    }
```

## destroy : [super](OperatorsAbstractComponent#destroy.md) ##

```java

83	    public void destroy(){
84	        super.destroy();
85
86	        this.colorOver = null;
87	        this.background = null;
88	        this.backgroundOver = null;
89	        this.stroke = null;
90	        this.strokeOver = null;
91	        this.shape = null;
92	    }
```

## resized : [super](OperatorsAbstractComponent#resized.md) ##

```java

94	    public void resized(){
95	        super.resized();
96
97	        this.layout();
98	    }
```

## modified : [super](OperatorsAbstractComponent#modified.md) ##

```java

100	    public void modified(){
101	        super.modified();
102
103	        this.layout();
104	    }
```

## layout ##

```java

124	    public void layout(Component.Layout.Order order){
125	        switch(order){
126	        case Content:
127	            break;
128	        case Parent:
129	            break;
130	        default:
131	            throw new IllegalStateException(order.name());
132	        }
133	        this.modified();
134	    }
```

## layout ##

```java

365	    protected void layout(){
366
367	        if (!this.fixed){
368
369	            this.setBoundsVectorInit(this.getParentVector());
370	        }
371
372	        Bounds bounds = this.getBoundsVector();
373	        switch(this.style){
374	        case SQUARE:
375	            this.shape = bounds;
376	            break;
377	        case ROUND:
378	            this.shape = bounds.round(this.arc);
379	            break;
380	        default:
381	            throw new IllegalStateException(this.style.name());
382	        }
383	    }
```
