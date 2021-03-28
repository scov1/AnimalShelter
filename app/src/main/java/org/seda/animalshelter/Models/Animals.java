package org.seda.animalshelter.Models;

public class Animals {

    private String animalId;
    private String animalImage;
    private String animalName;
    private String animalAge;
    private String animalGender;
    private String animalDesc;

    public Animals(String animalId,String animalImage, String name, String age, String gender, String desc) {
        this.animalId=animalId;
        this.animalImage = animalImage;
        this.animalName = name;
        this.animalAge = age;
        this.animalGender = gender;
        this.animalDesc = desc;
    }

//    public Animals(String animalImage, String name, String age, String gender) {
//        this.animalImage = animalImage;
//        this.name = name;
//        this.age = age;
//        this.gender = gender;
//    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getAnimalImage() {
        return animalImage;
    }

    public void setAnimalImage(String animalImage) {
        this.animalImage = animalImage;
    }

    public String getName() {
        return animalName;
    }

    public void setName(String name) {
        this.animalName = name;
    }

    public String getAge() {
        return animalAge;
    }

    public void setAge(String age) {
        this.animalAge = age;
    }

    public String getGender() {
        return animalGender;
    }

    public void setGender(String gender) {
        this.animalGender = gender;
    }

    public String getDesc() {
        return animalDesc;
    }

    public void setDesc(String desc) {
        this.animalDesc = desc;
    }
}
