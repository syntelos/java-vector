# [Container](OperatorsContainer.md) : [AbstractComponent](OperatorsAbstractComponent.md) #

> _Subclasses_
> [ContainerScroll](OperatorsContainerScroll.md)
> [Dialog](OperatorsDialog.md)
> [TableBig](OperatorsTableBig.md)
> [TableSmall](OperatorsTableSmall.md)
> [TextLayout](OperatorsTextLayout.md)

## init : [super](OperatorsAbstractComponent#init.md) ##

```java

65	    public void init(){
66	        super.init();
67
68	        this.fit = false;
69	    }
```

## destroy : [super](OperatorsAbstractComponent#destroy.md) ##

```java

71	    public void destroy(){
72	        super.destroy();
73	        try {
74	            for (Component c: this){
75	                c.destroy();
76	            }
77	        }
78	        finally {
79	            this.components = null;
80	        }
81	    }
```

## resized : [super](OperatorsAbstractComponent#resized.md) ##

```java

83	    public void resized(){
84	        super.resized();
85
86	        for (Component c: this){
87
88	            c.resized();
89	        }
90
91	        if (this.fit){
92
93	            this.fit();
94	        }
95	    }
```

## modified : [super](OperatorsAbstractComponent#modified.md) ##

```java

97	    public void modified(){
98	        super.modified();
99
100	        for (Component c: this){
101
102	            c.modified();
103	        }
104
105	        if (this.fit){
106
107	            this.fit();
108	        }
109	    }
```

## relocated : [super](OperatorsAbstractComponent#relocated.md) ##

```java

111	    public void relocated(){
112	        super.relocated();
113
114	        for (Component c: this){
115
116	            c.relocated();
117	        }
118
119	        if (this.fit){
120
121	            this.fit();
122	        }
123	    }
```

## fit ##

```java

457	    protected void fit(){
458
459	        if (0 < this.count()){
460
461	            final Bounds bounds = this.getBoundsVector();
462
463	            final Bounds children = this.queryBoundsContent();
464
465	            bounds.width = children.width;
466	            bounds.height = children.height;
467
468	            this.setBoundsVector(bounds);
469
470	            for (Component.Layout c : this.listParent(Component.Layout.class)){
471
472	                if (Layout.Order.Parent == c.queryLayout()){
473
474	                    c.resized();
475	                }
476	            }
477	        }
478	    }
```
