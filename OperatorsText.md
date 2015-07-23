# [Text](OperatorsText.md) : [BorderComponent](OperatorsBorderComponent.md) #

> _Subclasses_
> [Button](OperatorsButton.md)
> [TextEdit](OperatorsTextEdit.md)

## init : [super](OperatorsBorderComponent#init.md) ##

```java

74	    public void init(){
75	        super.init();
76
77	        this.font = Font.Default;
78	        this.color = Color.black;
79	        this.fill = true;
80	        this.fixed = false;
81	        this.cols = 25;
82	        this.padding.set(this.font);
83	    }
```

## destroy : [super](OperatorsBorderComponent#destroy.md) ##

```java

91	    public void destroy(){
92	        super.destroy();
93
94	        this.vector = null;
95	        this.shape = null;
96	        this.localPositions = null;
97	        this.colorOver = null;
98	        this.stroke = null;
99	        this.strokeOver = null;
100	    }
```

## resized : [super](OperatorsBorderComponent#resized.md) ##

```java

105	    public void resized(){
106	        super.resized();
107	        /*
108	         * Changes to transforms including location
109	         */
110	        this.layout();
111	    }
```

## relocated : [super](OperatorsBorderComponent#relocated.md) ##

```java

113	    public void relocated(){
114	        super.relocated();
115	        /*
116	         * Changes to transforms including location
117	         */
118	        this.layout();
119	    }
```

## modified : [super](OperatorsBorderComponent#modified.md) ##

```java

125	    public void modified(){
126	        super.modified();
127
128	        this.vector = null;
129	        this.shape = null;
130	        this.localPositions = null;
131
132	        this.layout();
133	    }
```

## layout ##

```java

140	    public void layout(Order order){
141	        switch(order){
142	        case Parent:
143	            this.fixed = false;
144	            break;
145	        case Content:
146	            this.fixed = true;
147	            break;
148	        default:
149	            throw new IllegalStateException(order.name());
150	        }
151	        this.modified();
152	    }
```

## layout ##

```java

582	    protected void layout(){
583
584	        if (null == this.shape && 0 < this.length()){
585
586	            final Point2D.Float baseline = this.getShapeBaseline();
587
588	            this.vector = this.font.createGlyphVector(this.string);
589
590	            this.shape = this.vector.getOutline(baseline.x,baseline.y);
591
592	            this.localPositions = null;
593	        }
594
595	        if (this.fixed){
596	            this.resizeToShapeArea();
597	            this.layoutScaleToShapeArea();
598	        }
599	        else {
600	            this.resizeToParent();
601	            this.layoutScaleToDimensions();
602	        }
603	    }
```
