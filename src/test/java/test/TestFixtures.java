package test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.github.fabiosoaza.salesimporter.infrastructure.record.ClientRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.ImportRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SaleRecord;
import com.github.fabiosoaza.salesimporter.infrastructure.record.SellerRecord;

public class TestFixtures {

	public static List<ImportRecord> defaultValidRecords() {
		return Arrays.asList(
				new SellerRecord("001", "1234567891234", "Pedro", new BigDecimal("50000")),
				new SellerRecord("001", "3245678865434", "Paulo", new BigDecimal("40000.99")),
				new ClientRecord("002", "2345675434544345", "Jose da Silva", "Rural"),
				new ClientRecord("002", "2345675433444345", "Eduardo Pereira", "Rural"),
				new SaleRecord("003", "10", "[1-10-100,2-30-2.50,3-40-3.10]", "Pedro"),
				new SaleRecord("003", "08", "[1-34-10,2-33-1.50,3-40-0.10]", "Paulo"),
				new SaleRecord("003", "05", "[1-34-1000,2-33-1.50,3-40-0.10]", "Fabio"),
				new SaleRecord("003", "04", "[1-65-1000,2-33-19.50,3-40-0.10]", "Paulo")
				);
	}
	
}
