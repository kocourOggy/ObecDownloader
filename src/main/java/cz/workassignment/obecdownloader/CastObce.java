/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.workassignment.obecdownloader;

/**
 *
 * @author stepaiv3
 */
public final class CastObce {

    public Integer Kod;
    public String Nazev;
    public Integer KodObce;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + String.valueOf(this.Kod) + "," + String.valueOf(this.Nazev) + "," + String.valueOf(this.KodObce) + ")";
    }
}
