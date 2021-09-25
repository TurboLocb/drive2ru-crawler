import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тест корректности работы класса AutoUtils")
public class AutoUtilsTest {

    @Test
    @DisplayName("должен корректно обрабатывать строку с автомобилем")
    void should_CorrectParseStringWithAutoNameAndBrand(){
        Assertions.assertEquals(AutoUtils.getAutoBrandByString("Продажа ГАЗ 31 — Нижний Новгород"), "ГАЗ 31");
    }

    @Test
    @DisplayName("должен возвращать модель автомобиля")
    void should_CorrectParseAutoModel_When_CurrentBrandIsExist(){
        Assertions.assertEquals(AutoUtils.getAutoModelByInputBrand("Range Rover Sport", "Range Rover"), "Sport");
    }

    @Test
    @DisplayName("должен возвращать объем двигателя автомобиля")
    void should_CorrectParseEngineCapacityByInputString(){
        Assertions.assertEquals(AutoUtils.getAutoEngineCapacityByInputString("Двигатель 2.0 бензиновый (150 л.с.)"), 2.0f);
    }

    @Test
    @DisplayName("должен возвращать год создания автомобиля")
    void should_CorrectParseAutoYearByInputString(){
        Assertions.assertEquals(AutoUtils.getAutoYearByInputString("Машина 2000 года выпуска, была куплена в 2020 году"), 2000);
    }

}
