package spark

import java.util.logging
import java.util.logging.Level

import akka.actor.ActorSystem
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.StreamingContext

import scala.collection.mutable
import scala.concurrent.duration._

class Scheduler(val lines: mutable.Queue[RDD[Int]], val streamingContext: StreamingContext) {
  private[this] val actorSystem = ActorSystem()
  private[this] val scheduler = actorSystem.scheduler
  private[this] implicit val executor = actorSystem.dispatcher

  initScheduler

  def initScheduler = {
    scheduler.schedule(
      initialDelay = DurationInt(4).seconds,
      interval = DurationInt(4).seconds,
      runnable = task
    )
  }
  
  def task: Runnable = {
    new Runnable {
      def run() {
        logging.Logger.getGlobal.log(Level.INFO, "I'm working")
        lines += streamingContext.sparkContext.makeRDD(List.range(8000, 9000))
        logging.Logger.getGlobal.log(Level.INFO, "Finished")
      }
    }
  }
}
