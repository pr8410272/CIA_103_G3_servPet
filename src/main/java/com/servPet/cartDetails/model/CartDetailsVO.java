package com.servPet.cartDetails.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.servPet.meb.model.MebVO;
import com.servPet.pdDetails.model.PdDetailsVO;

@Entity
@Table(name = "SHOPPING_CART_DETAILS")
//@IdClass(CartDetailsId.class)
public class CartDetailsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CART_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cartId;

	@ManyToOne
	@JoinColumn(name = "MEB_ID", referencedColumnName = "MEB_ID") // 後面的MEB_ID應該對應到Meb表格的欄位
	private MebVO mebVO;

	// MebVO對應的
//	@OneToMany(mappedBy = "mebVO")
//	private Set<CartDetailsVO> cartDetailsVO = new HashSet();

	@ManyToOne
	@JoinColumn(name = "PD_ID", referencedColumnName = "PD_ID")
	private PdDetailsVO pdDetailsVO;

	// PdDetailsVO對應的
//	@OneToMany(mappedBy = "pdDetailsVO")
//	private Set<CartDetailsVO> cartDetailsVO = new HashSet();

	@Column(name = "QUANTITY")
	private Integer quantity;

	// Default constructor
	public CartDetailsVO() {
	}

	// Constructor with all fields
	public CartDetailsVO(Integer cartId, MebVO mebVO, PdDetailsVO pdDetailsVO, Integer quantity) {
		this.cartId = cartId;
//		this.mebVO = mebVO;
		this.pdDetailsVO = pdDetailsVO;
		this.quantity = quantity;
	}

	// Getters and Setters
	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public MebVO getMebVO() {
		return mebVO;
	}

	public void setMebVO(MebVO mebVO) {
		this.mebVO = mebVO;
	}

	public PdDetailsVO getPdDetailsVO() {
		return pdDetailsVO;
	}

	public void setPdDetailsVO(PdDetailsVO pdDetailsVO) {
		this.pdDetailsVO = pdDetailsVO;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	// toString
	@Override
	public String toString() {
		return "CartDetailsVO{" + "cartId=" + cartId + ", mebVO=" + (mebVO != null ? mebVO.getMebId() : null)
				+ ", pdDetailsVO=" + (pdDetailsVO != null ? pdDetailsVO.getPdId() : null) + ", quantity=" + quantity
				+ '}';
	}

	// equals and hashCode
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		CartDetailsVO that = (CartDetailsVO) o;

		return cartId != null ? cartId.equals(that.cartId) : that.cartId == null;
	}

	@Override
	public int hashCode() {
		return cartId != null ? cartId.hashCode() : 0;
	}
}
