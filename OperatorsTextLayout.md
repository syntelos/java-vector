# [TextLayout](OperatorsTextLayout.md) : [Container](OperatorsContainer.md) #

> _Subclasses (none)_

## init : [super](OperatorsContainer#init.md) ##

```java

74	    public void init(){
75	        super.init();
76
77	        this.font = Font.Default;
78	        this.padding.set(this.font);
79	        this.wrap = false;
80	        this.debug = false;
81	    }
```

## resized : [super](OperatorsContainer#resized.md) ##

```java

83	    public void resized(){
84
85	        this.layout();
86
87	        super.resized();
88	    }
```

## modified : [super](OperatorsContainer#modified.md) ##

```java

90	    public void modified(){
91
92	        this.layout();
93
94	        super.modified();
95	    }
```

## relocated : [super](OperatorsContainer#relocated.md) ##

```java

97	    public void relocated(){
98
99	        this.layout();
100
101	        super.relocated();
102	    }
```

## parse ##

```java

221	    protected void parse(String text){
222	        final StringTokenizer strtok = new StringTokenizer(text," \t\n",true);
223
224	        final StringBuilder sp = new StringBuilder();
225
226	        while (strtok.hasMoreTokens()){
227	            String tok = strtok.nextToken();
228
229	            if (" ".equals(tok)){
230
231	                sp.append(' ');
232
233	                continue;
234	            }
235	            else if (0 < sp.length()){
236
237	                Text child = new Text();
238	                this.add(child);
239
240	                child.setFont(this.font);
241	                child.clearPadding();
242	                child.setText(sp.toString());
243	                child.setFixed(true);
244
245	                if (this.debug){
246	                    Border debug = new Border();
247
248	                    child.setBorder(debug);
249
250	                    debug.setColor(Color.red);
251	                    debug.setColorOver(Color.green);
252	                }
253
254	                child.modified();
255
256	                sp.setLength(0);
257	            }
258
259	            Text child = new Text();
260	            this.add(child);
261
262	            child.setFont(this.font);
263	            child.clearPadding();
264	            child.setText(tok);
265	            child.setFixed(true);
266
267	            if (this.debug){
268	                Border debug = new Border();
269
270	                child.setBorder(debug);
271
272	                debug.setColor(Color.red);
273	                debug.setColorOver(Color.green);
274	            }
275
276	            child.modified();
277	        }
278	    }
```

## layout ##

```java

279	    protected void layout(){
280	        Bounds shape;
281	        final float cr = this.padding.left;
282	        float lf = 0;
283	        float x = cr, y = this.padding.top, w, h;
284
285
286	        if (this.wrap){
287	            final Bounds bounds = this.getBoundsVector();
288
289	            for (Component.Layout.Text text: this.list(Component.Layout.Text.class)){
290
291	                text.layout(Component.Layout.Order.Content);
292
293	                shape = text.queryBoundsContent();
294
295	                w = (shape.x+shape.width);
296	                h = (shape.y+shape.height);
297
298	                shape.width = w;
299	                shape.height = h;
300
301	                if (0 == lf){
302	                    lf = h;
303	                    shape.x = x;
304	                    shape.y = y;
305	                    text.setBoundsVector(shape);
306	                    text.relocated();
307
308	                    x += w;
309	                }
310	                else {
311	                    lf = Math.max(lf,h);
312
313	                    if ((x+shape.width) <= bounds.width){
314	                        shape.x = x;
315	                        shape.y = y;
316	                        text.setBoundsVector(shape);
317	                        text.relocated();
318
319	                        x += w;
320	                    }
321	                    else {
322	                        x = cr;
323	                        y += lf;
324
325	                        shape.x = x;
326	                        shape.y = y;
327	                        text.setBoundsVector(shape);
328	                        text.relocated();
329	                    }
330	                }
331	            }
332	        }
333	        else {
334	            w = 0; h = 0;
335
336	            for (Component.Layout.Text text: this.list(Component.Layout.Text.class)){
337
338	                text.layout(Component.Layout.Order.Content);
339
340	                shape = text.queryBoundsContent();
341
342	                w = (shape.x+shape.width);
343	                h = (shape.y+shape.height);
344
345	                shape.x = x;
346	                shape.y = y;
347
348	                shape.width = w;
349	                shape.height = h;
350
351	                text.setBoundsVector(shape);
352	                text.relocated();
353
354	                final Component.Layout.Text.Whitespace type = text.queryLayoutText();
355
356	                switch(type){
357	                case Vertical:
358
359	                    x = cr;
360	                    y += lf;
361
362	                    lf = h;
363	                    break;
364	                default:
365
366	                    x += w;
367
368	                    lf = Math.max(lf,h);
369
370	                    break;
371	                }
372	            }
373	        }
374	    }
```
