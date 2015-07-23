# [ContainerScroll](OperatorsContainerScroll.md) : [Container](OperatorsContainer.md) #

> _Subclasses (none)_

## resized : [super](OperatorsContainer#resized.md) ##

```java

39	    public void resized(){
40
41	        this.fit = false;
42
43	        super.resized();
44
45	        this.layout();
46	    }
```

## modified : [super](OperatorsContainer#modified.md) ##

```java

48	    public void modified(){
49
50	        this.fit = false;
51
52	        super.modified();
53
54	        this.layout();
55	    }
```

## relocated : [super](OperatorsContainer#relocated.md) ##

```java

57	    public void relocated(){
58
59	        this.fit = false;
60
61	        super.relocated();
62
63	        this.layout();
64	    }
```

## layout ##

```java

69	    public void layout(Component.Layout.Order order){
70
71	        if (Component.Layout.Order.Parent != order)
72	            throw new UnsupportedOperationException();
73	        else
74	            this.modified();
75	    }
```

## layout ##

```java

169	    protected void layout(){
170
171	        this.setBoundsVectorInit(this.getParentVector());
172
173	        ContainerScrollPosition hor = null, ver = null;
174	        {
175	            final ContainerScrollPosition[] p = listContainerScrollPosition();
176	            if (null != p){
177	                switch(p.length){
178	                case 1:{
179	                    ContainerScrollPosition pp = p[0];
180	                    if (ContainerScrollPosition.Axis.Horizontal == pp.axis)
181	                        hor = pp;
182	                    else
183	                        ver = pp;
184
185	                    break;
186	                }
187	                case 2:
188	                    hor = p[0];
189	                    ver = p[1];
190	                    break;
191	                }
192	            }
193	        }
194
195	        if (null != hor || null != ver){
196
197	            Bounds children = this.queryBoundsContent();
198
199	            if (children.width > this.bounds.width){
200
201	                if (null != hor)
202	                    hor.layoutScroll(children);
203	            }
204	            else if (children.width < this.bounds.width){
205
206	                if (null != hor)
207	                    hor.layoutFit(children);
208	            }
209
210	            if (children.height > this.bounds.height){
211
212	                if (null != ver)
213	                    ver.layoutScroll(children);
214	            }
215	            else if (children.height < this.bounds.height){
216
217	                if (null != ver)
218	                    ver.layoutFit(children);
219	            }
220	        }
221	    }
```
