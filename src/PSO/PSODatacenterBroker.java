package PSO;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.lists.VmList;

import java.util.List;


public class PSODatacenterBroker extends DatacenterBroker {

    private double[] mapping;

    PSODatacenterBroker(String name) throws Exception {
        super(name);
    }

//    public void submitMapping(double[] mapping) {
//        this.mapping = mapping;
//    }

    public void setMapping(double[] mapping) {
        this.mapping = mapping;
    }

    @Override
    protected void submitCloudlets() {
        List<Cloudlet> tasks = assignCloudletsToVms(getCloudletList());
        int vmIndex = 0;
        for (Cloudlet cloudlet : tasks) {
            Vm vm;
            // if user didn't bind this cloudlet and it has not been executed yet
            if (cloudlet.getVmId() == -1) {
                vm = getVmsCreatedList().get(vmIndex);
            } else { // submit to the specific vm
                vm = VmList.getById(getVmsCreatedList(), cloudlet.getVmId());
                if (vm == null) { // vm was not created
                    Log.printLine(CloudSim.clock() + ": " + getName() + ": Postponing execution of cloudlet "
                            + cloudlet.getCloudletId() + ": bount VM not available");
                    continue;
                }
            }

            Log.printLine(CloudSim.clock() + ": " + getName() + ": Sending cloudlet "
                    + cloudlet.getCloudletId() + " to VM #" + vm.getId());
            cloudlet.setVmId(vm.getId());
            sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
            cloudletsSubmitted++;
            vmIndex = (vmIndex + 1) % getVmsCreatedList().size();
            getCloudletSubmittedList().add(cloudlet);
        }
    }


    public List<Cloudlet> assignCloudletsToVms(List<Cloudlet> cloudlist) {
        int idx = 0;
        for (Cloudlet cl : cloudlist) {
            cl.setVmId((int) mapping[idx++]);
        }
        return cloudlist;
    }
}
