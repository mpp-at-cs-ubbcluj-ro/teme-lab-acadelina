import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import repository.ComputerRepairRequestRepository;
import repository.ComputerRepairedFormRepository;
import repository.file.ComputerRepairRequestFileRepository;
import repository.file.ComputerRepairedFormFileRepository;
import repository.jdbc.ComputerRepairRequestJdbcRepository;
import repository.jdbc.ComputerRepairedFormJdbcRepository;
import services.ComputerRepairServices;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class RepairShopConfig {
    @Bean
    Properties getProps() {
        Properties prop= new Properties();
        try{
            System.out.println("Searching bd.config in directory"+((new File(".")).getAbsolutePath()));
            prop.load(new FileReader("bd.config"));
        }catch(IOException e){
            System.out.println("Configurare file bd.cong not found"+e.getMessage());
        }
        return prop;
    }

    @Bean
    ComputerRepairRequestRepository requestsRepo(){
       //return new ComputerRepairRequestJdbcRepository(getProps());
       return new ComputerRepairRequestFileRepository("ComputerRequests.txt");

    }

    @Bean
    ComputerRepairedFormRepository formsRepo(){
       //return new ComputerRepairedFormJdbcRepository(getProps());
        return new ComputerRepairedFormFileRepository("RepairedForms.txt", requestsRepo());

    }


    @Bean
    ComputerRepairServices services(){
       
        return  new ComputerRepairServices(requestsRepo(),formsRepo());
    }

}
