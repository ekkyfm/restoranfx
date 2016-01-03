package model;
// Generated Jan 3, 2016 7:01:35 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Menu generated by hbm2java
 */
@Entity
@Table(name="menu"
    ,catalog="restoran"
)
public class Menu  implements java.io.Serializable {


     private int idMenu;
     private String namaMenu;
     private String jenis;
     private int stok;
     private int harga;
     private Set detailPesanans = new HashSet(0);

    public Menu() {
    }

	
    public Menu(int idMenu, String namaMenu, String jenis, int stok, int harga) {
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.jenis = jenis;
        this.stok = stok;
        this.harga = harga;
    }
    public Menu(int idMenu, String namaMenu, String jenis, int stok, int harga, Set detailPesanans) {
       this.idMenu = idMenu;
       this.namaMenu = namaMenu;
       this.jenis = jenis;
       this.stok = stok;
       this.harga = harga;
       this.detailPesanans = detailPesanans;
    }
   
     @Id 

    
    @Column(name="id_menu", unique=true, nullable=false)
    public int getIdMenu() {
        return this.idMenu;
    }
    
    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    
    @Column(name="nama_menu", nullable=false, length=60)
    public String getNamaMenu() {
        return this.namaMenu;
    }
    
    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    
    @Column(name="jenis", nullable=false, length=8)
    public String getJenis() {
        return this.jenis;
    }
    
    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    
    @Column(name="stok", nullable=false)
    public int getStok() {
        return this.stok;
    }
    
    public void setStok(int stok) {
        this.stok = stok;
    }

    
    @Column(name="harga", nullable=false)
    public int getHarga() {
        return this.harga;
    }
    
    public void setHarga(int harga) {
        this.harga = harga;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="menu")
    public Set getDetailPesanans() {
        return this.detailPesanans;
    }
    
    public void setDetailPesanans(Set detailPesanans) {
        this.detailPesanans = detailPesanans;
    }




}


