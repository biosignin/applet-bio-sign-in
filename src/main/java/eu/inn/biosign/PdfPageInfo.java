package eu.inn.biosign;

public class PdfPageInfo {

	private int totalPage;

	private int pointWidth;

	private String imgB64;

	public PdfPageInfo(int totalPage, int pointWidth, String imgB64) {
		super();
		this.totalPage = totalPage;
		this.pointWidth = pointWidth;
		this.imgB64 = imgB64;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPointWidth() {
		return pointWidth;
	}

	public void setPointWidth(int pointWidth) {
		this.pointWidth = pointWidth;
	}

	public String getImgB64() {
		return imgB64;
	}

	public void setImgB64(String imgB64) {
		this.imgB64 = imgB64;
	}

}
