# [Display](OperatorsDisplay.md) : java.awt.Canvas #

> _Subclasses (none)_

## init ##

```java

91	    public void init(){
92
93	        this.destroy();
94
95	        this.mouseIn = false;
96
97	        this.transform.init();
98
99	        this.layout();
100	    }
```

## init ##

```java

101	    protected void init(Boolean init){
102	        if (null != init && init.booleanValue()){
103
104	            this.init();
105	        }
106	    }
```

## destroy ##

```java

107	    public void destroy(){
108	        this.outputOverlayAnimateCancel();
109	        try {
110	            for (Component c: this){
111	                c.destroy();
112	            }
113	        }
114	        finally {
115	            this.components = null;
116	            this.boundsNative = null;
117	            this.boundsUser = null;
118
119	            this.flush();
120	        }
121	    }
```

## resized ##

```java

122	    public void resized(){
123
124	        this.layout();
125
126	        for (Component c: this){
127
128	            c.resized();
129	        }
130	    }
```

## modified ##

```java

131	    public void modified(){
132
133	        this.layout();
134
135	        for (Component c: this){
136
137	            c.modified();
138	        }
139	    }
```

## relocated ##

```java

140	    public void relocated(){
141	    }
```

## flush ##

```java

142	    protected void flush(){
143
144	        this.output.flush();
145	    }
```

## shown ##

```java

650	    protected void shown(){
651
652	        this.outputOverlayAnimateResume();
653	    }
```

## hidden ##

```java

654	    protected void hidden(){
655
656	        this.outputOverlayAnimateSuspend();
657	    }
```

## layout ##

```java

659	    public void layout(){
660	        if (null == this.boundsNative){
661	            this.boundsNative = this.getBounds();
662	        }
663	        if (null == this.boundsUser){
664	            this.boundsUser = new Bounds(this.boundsNative);
665	        }
666	        this.transform.scaleToAbsolute(this.boundsUser,this.boundsNative);
667	    }
```
