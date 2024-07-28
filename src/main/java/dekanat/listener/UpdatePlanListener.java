package dekanat.listener;


import dekanat.model.PlansModel;

import java.util.List;

public interface UpdatePlanListener {
    void onUpdate(PlansModel plansModel, List<String> students);
}
