package com.utils.ClassBuilder;

import javafx.util.Builder;

public class Meal {
    private final Boolean apple;
    private final Boolean rise;//必须有的
    private final Boolean meat;
    private final Boolean tomato;//必须有的
    private final Boolean soup;
    private final Boolean tea;
    public static class builder{
        private Boolean apple=false;
        private final Boolean rise;//必须有的
        private Boolean meat=false;
        private final Boolean tomato;//必须有的
        private Boolean soup=false;
        private Boolean tea=false;
        public builder(Boolean rise,Boolean tomato){
            this.rise=rise;
            this.tomato=tomato;
        }
        private builder meat(Boolean meat){
            this.meat=meat;
            return this;
        }
        private builder apple(Boolean apple){
            this.apple=apple;
            return this;
        }
        private builder soup(Boolean soup){
            this.soup=soup;
            return this;
        }
        private builder tea(Boolean tea){
            this.tea=tea;
            return this;
        }
        private Meal build(){
            return new Meal(this);
        }

    }
    private Meal(builder builder){
        this.rise=builder.rise;
        this.tomato=builder.tomato;
        this.apple=builder.apple;
        this.soup=builder.soup;
        this.tea=builder.tea;
        this.meat=builder.meat;
    }

    public static void main(String[] args) {
        Meal.builder mealBuilder=new Meal.builder(true,true);
        Meal meal= mealBuilder.apple(false).build();
    }
}

