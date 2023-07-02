package wsb.project.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Klasa reprezentująca przedmiot w systemie. Umożliwia przechowywanie i zarządzanie informacjami
 * o przedmiocie, takimi jak identyfikator, wersja, indeks, opis, cena, ilość oraz daty modyfikacji i utworzenia.
 */
public class Item {
	
	private Integer id;
	private Integer version;
	private String index;
	private String description;
	private BigDecimal price;
	private Integer quantity;
	private LocalDateTime modified;
	private LocalDateTime created;
	
	/**
	 * Domyślny konstruktor, który tworzy pusty obiekt klasy Item.
	 */
	public Item() {
	
	}
	
	/**
	 * Konstruktor klasy Item, umożliwiający inicjalizację wszystkich pól klasy.
	 *
	 * @param id          Identyfikator przedmiotu.
	 * @param version     Wersja przedmiotu.
	 * @param index       Indeks przedmiotu.
	 * @param description Opis przedmiotu.
	 * @param price       Cena przedmiotu.
	 * @param quantity    Ilość przedmiotu.
	 * @param modified    Data ostatniej modyfikacji przedmiotu.
	 * @param created     Data utworzenia przedmiotu.
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
	 * @return Zwraca identyfikator przedmiotu.
	 */
	public Integer getId() {
		
		return id;
	}
	
	/**
	 * Ustawia identyfikator przedmiotu.
	 *
	 * @param id Identyfikator do ustawienia.
	 */
	public void setId(Integer id) {
		
		this.id = id;
	}
	
	/**
	 * @return Zwraca wersję przedmiotu.
	 */
	public Integer getVersion() {
		
		return version;
	}
	
	/**
	 * Ustawia wersję przedmiotu.
	 *
	 * @param version Wersja do ustawienia.
	 */
	public void setVersion(Integer version) {
		
		this.version = version;
	}
	
	/**
	 * @return Zwraca indeks przedmiotu.
	 */
	public String getIndex() {
		
		return index;
	}
	
	/**
	 * Ustawia indeks przedmiotu.
	 *
	 * @param index Indeks do ustawienia.
	 */
	public void setIndex(String index) {
		
		this.index = index;
	}
	
	/**
	 * @return Zwraca opis przedmiotu.
	 */
	public String getDescription() {
		
		return description;
	}
	
	/**
	 * Ustawia opis przedmiotu.
	 *
	 * @param description Opis do ustawienia.
	 */
	public void setDescription(String description) {
		
		this.description = description;
	}
	
	/**
	 * @return Zwraca cenę przedmiotu.
	 */
	public BigDecimal getPrice() {
		
		return price;
	}
	
	/**
	 * Ustawia cenę przedmiotu.
	 *
	 * @param price Cena do ustawienia.
	 */
	public void setPrice(BigDecimal price) {
		
		this.price = price;
	}
	
	/**
	 * @return Zwraca ilość przedmiotu.
	 */
	public Integer getQuantity() {
		
		return quantity;
	}
	
	/**
	 * Ustawia ilość przedmiotu.
	 *
	 * @param quantity Ilość do ustawienia.
	 */
	public void setQuantity(Integer quantity) {
		
		this.quantity = quantity;
	}
	
	/**
	 * @return Zwraca datę ostatniej modyfikacji przedmiotu.
	 */
	public LocalDateTime getModified() {
		
		return modified;
	}
	
	/**
	 * Ustawia datę ostatniej modyfikacji przedmiotu.
	 *
	 * @param modified Data ostatniej modyfikacji do ustawienia.
	 */
	public void setModified(LocalDateTime modified) {
		
		this.modified = modified;
	}
	
	/**
	 * @return Zwraca datę utworzenia przedmiotu.
	 */
	public LocalDateTime getCreated() {
		
		return created;
	}
	
	/**
	 * Ustawia datę utworzenia przedmiotu.
	 *
	 * @param created Data utworzenia do ustawienia.
	 */
	public void setCreated(LocalDateTime created) {
		
		this.created = created;
	}
	
	/**
	 * @return Zwraca tekstową reprezentację obiektu klasy Item.
	 */
	@Override
	public String toString() {
		
		return "Item{" + "id=" + id + ", version=" + version + ", name='" + index + '\'' + ", description='" +
				description + '\'' + ", price=" + price + ", quantity=" + quantity + ", modified=" + modified +
				", created=" + created + '}';
	}
}
