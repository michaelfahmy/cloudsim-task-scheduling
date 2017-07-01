package PSO;

import net.sourceforge.jswarm_pso.Particle;
import net.sourceforge.jswarm_pso.ParticleUpdate;
import net.sourceforge.jswarm_pso.Swarm;
import utils.Constants;

public class SchedulerParticleUpdate extends ParticleUpdate {
    private static final double W = 0.9;
    private static final double C = 2.0;

    SchedulerParticleUpdate(Particle particle) {
        super(particle);
    }

    @Override
    public void update(Swarm swarm, Particle particle) {
        double[] v = particle.getVelocity();
        double[] x = particle.getPosition();
        double[] pbest = particle.getBestPosition();
        double[] gbest = swarm.getBestPosition();

        for (int i = 0; i < Constants.NO_OF_TASKS; ++i) {
            v[i] = W * v[i] + C * Math.random() * (pbest[i] - x[i]) + C * Math.random() * (gbest[i] - x[i]);
            x[i] = (int) (x[i] + v[i]);
        }
    }
}