# [TableBig](OperatorsTableBig.md) : [Container](OperatorsContainer.md) #

> _Subclasses (none)_

## init : [super](OperatorsContainer#init.md) ##

```java

51	    public void init(){
52	        super.init();
53
54	        this.cellSpacing = 0f;
55	        this.cellWidth = 0f;
56	        this.cellHeight = 0f;
57	        this.fixed = false;
58	        this.rows = 0;
59	        this.cols = 0;
60	    }
```

## modified : [super](OperatorsContainer#modified.md) ##

```java

62	    public void modified(){
63	        super.modified();
64
65	        this.layout();
66	    }
```

## resized : [super](OperatorsContainer#resized.md) ##

```java

68	    public void resized(){
69	        super.resized();
70
71	        this.layout();
72	    }
```

## layout ##

```java

79	    public void layout(Component.Layout.Order order){
80	        switch(order){
81	        case Content:
82	            this.fixed = true;
83	            break;
84	        case Parent:
85	            this.fixed = false;
86	            break;
87	        default:
88	            throw new IllegalStateException(order.name());
89	        }
90	        this.modified();
91	    }
```

## layout ##

```java

92	    protected void layout(){
93
94	        if (!this.fixed){
95	            this.setBoundsVectorInit(this.getParentVector());
96	        }
97	        final int count = this.count();
98	        if (0 < count){
99	            final Bounds bounds = this.getBoundsVector();
100	            if (!bounds.isEmpty()){
101	                if (2 > count){
102	                    this.cols = 1;
103	                    this.rows = 1;
104	                }
105	                else {
106	                    this.cols = (int)Math.ceil(Math.sqrt(count));
107	                    this.rows = (count/this.cols);
108	                }
109	                final double ww = ((double)bounds.width-((double)this.cellSpacing*(double)(this.cols+1)));
110	                final double hh = ((double)bounds.height-((double)this.cellSpacing*(double)(this.rows+1)));
111
112	                this.cellWidth = (ww / (double)this.cols);
113	                this.cellHeight = (hh / (double)this.rows);
114
115
116	                final double x0 = this.cellSpacing;
117	                final double dx = (x0+this.cellWidth);
118	                final double dy = (x0+this.cellHeight);
119
120	                double xx = x0, yy = x0;
121
122	                int  rr = 0, cc = 0, cx;
123
124	                layout:
125	                for (rr = 0; rr < this.rows; rr++){
126
127	                    for (cc = 0; cc < this.cols; cc++){
128
129	                        cx = ((rr*this.cols)+cc);
130
131	                        if (this.has(cx)){
132
133	                            Component c = this.get(cx);
134
135	                            c.setBoundsVector(new TableCell(rr,cc,xx,yy,this.cellWidth,this.cellHeight));
136	                            c.resized();
137
138	                            xx += dx;
139	                        }
140	                        else
141	                            break layout;
142	                    }
143
144	                    xx = x0;
145	                    yy += dy;
146	                }
147	            }
148	        }
149	    }
```
