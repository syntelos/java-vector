/*
 * Copyright (c) 2009 John Pritchard, all rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Copyright (C) 1999 John Donohue.  All rights reserved.
 * 
 * Used with permission.
 */
package llg;

import java.awt.Event;
import java.awt.Graphics2D;

/**
 * 
 */
public class ExplodingLander 
    extends Lander
{
    private java.util.Random generator = new java.util.Random();


    ExplodingLander (Lander lander, Tickable collision){
        super(lander);

        collision.crash();

        mainRocketOperating = false;
        saveTimeWeLandedOrCrashed = platform.Clock.currentTimeMillis();
        if (!lander.intro){
            Game.Instance.landerCrash();
        }

        for (int cc = 0, count = this.lines.length; cc < count; cc++ ){
            Line line = this.lines[cc];
            double r = generator.nextFloat();
            r -= 0.5;
            r *= 5.0;
            line.deltaX += r;

            r = generator.nextFloat();
            r *= -4.0;
            line.deltaY += r;

            line.tick();
        }
        this.tx = 0d;
        this.ty = 0d;
    }


    public boolean isCrashed(){
        return true;
    }
    public void tick(){

        for (int cc = 0, count = this.lines.length; cc < count; cc++ ){
            Line line = this.lines[cc];

            line.deltaX += gravityAcceleration;

            line.deltaY += gravityAcceleration;

            line.tick();
        }
    }
    public void keyDown(Event evt, int key){
        if ( platform.Clock.currentTimeMillis() > (saveTimeWeLandedOrCrashed + 1000) ){
            if (0d < fuel)
                Game.Instance.newFlight();
        }
    }
}
