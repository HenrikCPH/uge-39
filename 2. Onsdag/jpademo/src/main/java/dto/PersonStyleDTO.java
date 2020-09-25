/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author Henne
 */
public class PersonStyleDTO {
    
    private String name;
    private int year;
    private String SwimStyle;

    public PersonStyleDTO(String name, int year, String SwimStyle) {
        this.name = name;
        this.year = year;
        this.SwimStyle = SwimStyle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSwimStyle() {
        return SwimStyle;
    }

    public void setSwimStyle(String SwimStyle) {
        this.SwimStyle = SwimStyle;
    }
    
    
    
}
