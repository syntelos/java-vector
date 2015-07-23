# [TableSmall](OperatorsTableSmall.md) : [Container](OperatorsContainer.md) #

> _Subclasses (none)_

## init : [super](OperatorsContainer#init.md) ##

```java

49	    public void init(){
50	        super.init();
51
52	        this.cellSpacing = 0f;
53	        this.cols = 2;
54	    }
```

## modified : [super](OperatorsContainer#modified.md) ##

```java

56	    public void modified(){
57
58	        this.fit = false;
59
60	        super.modified();
61
62	        this.layout();
63	    }
```

## resized : [super](OperatorsContainer#resized.md) ##

```java

65	    public void resized(){
66
67	        this.fit = false;
68
69	        super.resized();
70
71	        this.layout();
72	    }
```

## layout ##

```java

77	    public void layout(Component.Layout.Order order){
78
79	        if (Component.Layout.Order.Content != order)
80	            throw new UnsupportedOperationException();
81	        else
82	            this.modified();
83	    }
```

## layout ##

```java

84	    protected void layout(){
85
86	        final int count = this.count();
87	        if (0 < count){
88	            final int cols = this.cols;
89	            if (0 < cols){
90	                final int rows = (int)Math.ceil((float)count/(float)cols);
91
92	                final double cs = this.cellSpacing;
93
94	                final double[] colwidths = new double[cols];
95	                final double[] rowheights = new double[rows];
96
97	                Arrays.fill(colwidths,0.0);
98	                Arrays.fill(rowheights,0.0);
99
100	                int  rr, cc, cx;
101
102	                measurement:
103	                for (rr = 0; rr < rows; rr++){
104
105	                    for (cc = 0; cc < cols; cc++){
106
107	                        cx = ((rr*cols)+cc);
108
109	                        if (this.has(cx)){
110
111	                            Component c = this.get(cx);
112	                            Bounds cb = c.getBoundsVector();
113
114	                            colwidths[cc] = Math.max(colwidths[cc],(cs+cb.width));
115	                            rowheights[rr] = Math.max(rowheights[rr],(cs+cb.height));
116	                        }
117	                        else
118	                            break measurement;
119	                    }
120	                }
121
122
123	                double dx, dy, xx = cs, yy = cs;
124
125	                double tableWidth = (2.0*cs), tableHeight = tableWidth;
126
127	                definition:
128	                for (rr = 0; rr < rows; rr++){
129
130	                    dy = rowheights[rr];
131	                    tableHeight += dy;
132
133	                    for (cc = 0; cc < cols; cc++){
134
135	                        dx = colwidths[cc];
136
137	                        if (0 == rr){
138
139	                            tableWidth += dx;
140	                        }
141	                        cx = ((rr*cols)+cc);
142
143	                        if (this.has(cx)){
144
145	                            Component c = this.get(cx);
146	                            Bounds cb = c.getBoundsVector();
147	                            c.setBoundsVector(new TableCell(rr,cc,xx,yy,cb.width,cb.height));
148	                            c.relocated();
149
150	                            xx += dx;
151	                        }
152	                        else
153	                            break definition;
154	                    }
155	                    xx = cs;
156	                    yy += dy;
157	                }
158
159	                final Bounds bounds = this.getBoundsVector();
160	                bounds.width = (float)tableWidth;
161	                bounds.height = (float)tableHeight;
162
163	                this.setBoundsVector(bounds);
164	            }
165	        }
166	    }
```
