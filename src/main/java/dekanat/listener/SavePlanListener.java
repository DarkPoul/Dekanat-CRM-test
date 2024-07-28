package dekanat.listener;

import dekanat.model.PlansModel;

import java.util.List;

public interface SavePlanListener {
    void onSave(PlansModel plansModel, List<String> students);
}
