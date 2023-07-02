package wsb.project.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Klasa reprezentująca przedmiot (Item) z właściwościami takimi jak id, wersja,
 * indeks, opis, cena, ilość, oraz datami modyfikacji i utworzenia.
 */
public class Item {
	
	Integer id;
	Integer version;
	String index;
	String description;
	BigDecimal price;
	Integer quantity;
	LocalDateTime modified;
	LocalDateTime created;
	
	/**
	 * Domyślny konstruktor, tworzy nowy, pusty obiekt klasy Item.
	 */
	public Item() {
	
	}
	
	/**
	 * Konstruktor tworzący nowy obiekt klasy Item z określonymi wartościami pól.
	 *
	 * @param id          identyfikator przedmiotu.
	 * @param version     wersja przedmiotu.
	 * @param index       indeks przedmiotu.
	 * @param description opis przedmiotu.
	 * @param price       cena przedmiotu.
	 * @param quantity    ilość przedmiotu.
	 * @param modified    data ostatniej modyfikacji.
	 * @param created     data utworzenia.
	 */
	public Item(Integer id, Integer version, String index, String description, BigDecimal price, Integer quantity,
			LocalDateTime modified, LocalDateTime created) {
		
		this.id = id;
		this.version = version;
		this.index = index;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.modified = modified;
		this.created = created;
	}
	
	/**
	 * Zwraca identyfikator przedmiotu.
	 *
	 * @return identyfikator przedmiotu.
	 */
	public Integer getId() {
		
		return id;
	}
	
	/**
	 * Ustawia identyfikator przedmiotu.
	 *
	 * @param id identyfikator do ustawienia.
	 */
	public void setId(Integer id) {
		
		this.id = id;
	}
	
	/**
	 * Zwraca wersję przedmiotu.
	 *
	 * @return wersja przedmiotu.
	 */
	public Integer getVersion() {
		
		return version;
	}
	
	/**
	 * Ustawia wersję przedmiotu.
	 *
	 * @param version wersja do ustawienia.
	 */
	public void setVersion(Integer version) {
		
		this.version = version;
	}
	
	/**
	 * Zwraca indeks przedmiotu.
	 *
	 * @return indeks przedmiotu.
	 */
	public String getIndex() {
		
		return index;
	}
	
	/**
	 * Ustawia indeks przedmiotu.
	 *
	 * @param index indeks do ustawienia.
	 */
	public void setIndex(String index) {
		
		this.index = index;
	}
	
	/**
	 * Zwraca opis przedmiotu.
	 *
	 * @return opis przedmiotu.
	 */
	public String getDescription() {
		
		return description;
	}
	
	/**
	 * Ustawia opis przedmiotu.
	 *
	 * @param description opis do ustawienia.
	 */
	public void setDescription(String description) {
		
		this.description = description;
	}
	
	/**
	 * Zwraca cenę przedmiotu.
	 *
	 * @return cena przedmiotu.
	 */
	public BigDecimal getPrice() {
		
		return price;
	}
	
	/**
	 * Ustawia cenę przedmiotu.
	 *
	 * @param price cena do ustawienia.
	 */
	public void setPrice(BigDecimal price) {
		
		this.price = price;
	}
	
	/**
	 * Zwraca ilość przedmiotu.
	 *
	 * @return ilość przedmiotu.
	 */
	public Integer getQuantity() {
		
		return quantity;
	}
	
	/**
	 * Ustawia ilość przedmiotu.
	 *
	 * @param quantity ilość do ustawienia.
	 */
	public void setQuantity(Integer quantity) {
		
		this.quantity = quantity;
	}
	
	/**
	 * Zwraca datę ostatniej modyfikacji przedmiotu.
	 *
	 * @return data ostatniej modyfikacji.
	 */
	public LocalDateTime getModified() {
		
		return modified;
	}
	
	/**
	 * Ustawia datę ostatniej modyfikacji przedmiotu.
	 *
	 * @param modified data ostatniej modyfikacji do ustawienia.
	 */
	public void setModified(LocalDateTime modified) {
		
		this.modified = modified;
	}
	
	/**
	 * Zwraca datę utworzenia przedmiotu.
	 *
	 * @return data utworzenia.
	 */
	public LocalDateTime getCreated() {
		
		return created;
	}
	
	/**
	 * Ustawia datę utworzenia przedmiotu.
	 *
	 * @param created data utworzenia do ustawienia.
	 */
	public void setCreated(LocalDateTime created) {
		
		this.created = created;
	}
}
