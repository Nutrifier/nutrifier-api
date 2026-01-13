package fi.nutrifier.services;

import fi.nutrifier.entities.MealPlan;
import fi.nutrifier.entities.User;
import fi.nutrifier.repositories.UserMealPlanRepository;
import fi.nutrifier.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserMealPlanService {

    private final UserRepository userRepository;
    private final UserMealPlanRepository userMealPlanRepository;

    @Autowired
    public UserMealPlanService(UserRepository userRepository, UserMealPlanRepository userMealPlanRepository) {
        this.userRepository = userRepository;
        this.userMealPlanRepository = userMealPlanRepository;
    }

    public ResponseEntity<List<MealPlan>> getAllByUserId(String userId) {
        try {
            List<MealPlan> mealPlanList = userMealPlanRepository.findByUserId(userId).orElse(null);

            if (mealPlanList == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(mealPlanList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<MealPlan> create(String userId, MealPlan mealPlan) {
        try {
            User user = userRepository.findById(userId).orElse(null);

            List<MealPlan> mealPlans = userMealPlanRepository.findByUserId(userId).orElse(null);

            if (mealPlans == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            mealPlan.setUser(user);

            mealPlans.add(mealPlan);
            MealPlan createdMealPlan = userMealPlanRepository.save(mealPlan);

            return new ResponseEntity<>(createdMealPlan, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<MealPlan> update(String userId, MealPlan mealPlan) {
        try {
            List<MealPlan> mealPlans = userMealPlanRepository.findByUserId(userId).orElse(null);

            if (mealPlans == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            MealPlan newMealPlan = userMealPlanRepository.save(mealPlan);

            return ResponseEntity.ok(newMealPlan);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> delete(String id) {
        try {
            userMealPlanRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
