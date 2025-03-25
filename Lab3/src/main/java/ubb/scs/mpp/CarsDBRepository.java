package ubb.scs.mpp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository{

    private JdbcUtils dbUtils;



    private static final Logger logger= LogManager.getLogger();

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ",props);
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Car> cars=new ArrayList<>();
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from Masini where Manufacturer=?")){
            preparedStatement.setString(1, manufacturerN);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int id=resultSet.getInt("id");
                    String manufacturer=resultSet.getString("Manufacturer");
                    String model=resultSet.getString("Model");
                    int year=resultSet.getInt("Year");
                    Car car=new Car(manufacturer,model,year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error db "+e.getMessage());
        }
        logger.traceExit();
        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Car> cars=new ArrayList<>();
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from Masini where Year>? and Year<?")){
            preparedStatement.setInt(1, min);
            preparedStatement.setInt(2, max);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int id=resultSet.getInt("id");
                    String manufacturer=resultSet.getString("Manufacturer");
                    String model=resultSet.getString("Model");
                    int year=resultSet.getInt("Year");
                    Car car=new Car(manufacturer,model,year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error db "+e.getMessage());
        }
        logger.traceExit();
        return cars;
    }

    @Override
    public void add(Car elem) {
        logger.traceEntry("saving task {}", elem);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preparedStatement=con.prepareStatement("insert into Masini (manufacturer, model, year) values (?,?,?)")){
            preparedStatement.setString(1, elem.getManufacturer());
            preparedStatement.setString(2, elem.getModel());
            preparedStatement.setInt(3, elem.getYear());
            int result=preparedStatement.executeUpdate();
            logger.trace("Saved {} instances",result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error db "+e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Car elem) {
        logger.traceEntry("updating task {}", elem);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preparedStatement=con.prepareStatement("update Masini set manufacturer=?, model=?, year=? where id=?")){
            preparedStatement.setString(1, elem.getManufacturer());
            preparedStatement.setString(2, elem.getModel());
            preparedStatement.setInt(3, elem.getYear());
            preparedStatement.setInt(4, integer);
            int result=preparedStatement.executeUpdate();
            logger.trace("Updated {} instances",result);
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error db "+e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public Iterable<Car> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Car> cars=new ArrayList<>();
        try(PreparedStatement preparedStatement=con.prepareStatement("select * from Masini ")){
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int id=resultSet.getInt("id");
                    String manufacturer=resultSet.getString("Manufacturer");
                    String model=resultSet.getString("Model");
                    int year=resultSet.getInt("Year");
                    Car car=new Car(manufacturer,model,year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error db "+e.getMessage());
        }
        logger.traceExit();
        return cars;

    }
}
