package br.com.rf.dropchallenge.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class Beer implements Serializable {
    public static final int TYPE_LIGHT = 0;
    public static final int TYPE_MEDIUM = 1;
    public static final int TYPE_STRONG = 2;
    public static final int TYPE_RANDOM = 3;

    public String name;
    public String abv;
    public String description;
    public Ingredients ingredients;
    public Method method;
    public String image_url;

    public String getAbv() {
        return "ABV: " + abv;
    }

    public class Method implements Serializable {
        public List<MashTemp> mash_temp;

        public class MashTemp implements Serializable {
            public Amount temp;
            public Integer duration;
        }
    }


    public class Ingredients implements Serializable {

        public List<Hops> hops;
        public List<Malt> malt;

        public class Hops implements Serializable {
            public String name;
            public Amount amount;

            public String getName() {
                return !TextUtils.isEmpty(name) ? name : "";
            }

        }

        public class Malt implements Serializable {
            public String name;
            public Amount amount;

            public String getName(){
                return !TextUtils.isEmpty(name) ? name : "";
            }

        }

    }
}

