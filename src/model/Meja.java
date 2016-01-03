package model;
// Generated Jan 3, 2016 7:01:35 AM by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Meja generated by hbm2java
 */
@Entity
@Table(name="meja"
    ,catalog="restoran"
)
public class Meja  implements java.io.Serializable {


     private Integer noMeja;
     private String kategori;
     private int lantai;
     private int status;
     private int kapasitas;

    public Meja() {
    }

    public Meja(String kategori, int lantai, int status, int kapasitas) {
       this.kategori = kategori;
       this.lantai = lantai;
       this.status = status;
       this.kapasitas = kapasitas;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="no_meja", unique=true, nullable=false)
    public Integer getNoMeja() {
        return this.noMeja;
    }
    
    public void setNoMeja(Integer noMeja) {
        this.noMeja = noMeja;
    }

    
    @Column(name="kategori", nullable=false, length=10)
    public String getKategori() {
        return this.kategori;
    }
    
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    
    @Column(name="lantai", nullable=false)
    public int getLantai() {
        return this.lantai;
    }
    
    public void setLantai(int lantai) {
        this.lantai = lantai;
    }

    
    @Column(name="status", nullable=false)
    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }

    
    @Column(name="kapasitas", nullable=false)
    public int getKapasitas() {
        return this.kapasitas;
    }
    
    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }




}


