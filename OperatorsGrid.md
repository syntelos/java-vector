# [Grid](OperatorsGrid.md) : [AbstractComponent](OperatorsAbstractComponent.md) #

> _Subclasses (none)_

## init : [super](OperatorsAbstractComponent#init.md) ##

```java

66	    public void init(){
67	        super.init();
68
69	        this.color = Color.black;
70	        this.mouse = false;
71	        this.fixed = false;
72	    }
```

## destroy : [super](OperatorsAbstractComponent#destroy.md) ##

```java

74	    public void destroy(){
75	        super.destroy();
76
77	        this.domain = null;
78	        this.range = null;
79
80	        this.shape = null;
81	        this.pointer = null;
82	    }
```

## resized : [super](OperatorsAbstractComponent#resized.md) ##

```java

87	    public void resized(){
88	        super.resized();
89
90	        this.pointer = null;
91
92	        this.layout();
93	    }
```

## modified : [super](OperatorsAbstractComponent#modified.md) ##

```java

99	    public void modified(){
100	        super.modified();
101
102	        this.pointer = null;
103
104	        this.layout();
105	    }
```

## relocated : [super](OperatorsAbstractComponent#relocated.md) ##

```java

107	    public void relocated(){
108	        super.relocated();
109
110	        this.pointer = null;
111
112	        this.layout();
113	    }
```

## layout ##

```java

125	    public void layout(Component.Layout.Order order){
126	        switch(order){
127	        case Content:
128	            break;
129	        case Parent:
130	            break;
131	        default:
132	            throw new IllegalStateException(order.name());
133	        }
134	        this.modified();
135	    }
```

## layout ##

```java

287	    protected void layout(){
288
289	        if (!this.fixed){
290
291	            Component parent = this.getParentVector();
292
293	            this.setBoundsVectorInit(parent);
294	            this.setTransformLocal(parent.getTransformLocal());
295	        }
296
297	        final Bounds bounds = this.getBoundsVector();
298	        if (!bounds.isEmpty()){
299	            float[] range = this.range;
300	            if (null == range){
301	                range = Default(bounds.height);
302	            }
303	            float[] domain = this.domain;
304	            if (null == domain){
305	                domain = Default(bounds.width);
306	            }
307
308	            final float x0 = 0f;
309	            final float x1 = bounds.width;
310	            final float y0 = 0f;
311	            final float y1 = bounds.height;
312
313	            final Path2D.Float shape = new Path2D.Float();
314
315	            for (float x: domain){
316	                shape.moveTo(x,y0);
317	                shape.lineTo(x,y1);
318	            }
319	            for (float y: range){
320	                shape.moveTo(x0,y);
321	                shape.lineTo(x1,y);
322	            }
323	            this.shape = shape;
324	        }
325	    }
```
