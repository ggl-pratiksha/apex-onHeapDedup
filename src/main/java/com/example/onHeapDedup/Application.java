/**
 * Put your copyright and license info here.
 */
package com.example.onHeapDedup;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.DAG;
import com.datatorrent.lib.io.ConsoleOutputOperator;

@ApplicationAnnotation(name="OnHeapDedup")
public class Application implements StreamingApplication
{

  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    SerialNumberGenerator serialNumberGenerator = dag.addOperator("SerialNumberGenerator", SerialNumberGenerator.class);
    OnHeapDeduper deduper = dag.addOperator("OnDedupOperator", new OnHeapDeduper());
    ConsoleOutputOperator uniqueconsole = dag.addOperator("uniqueConsole", new ConsoleOutputOperator());
    ConsoleOutputOperator duplicateConsole = dag.addOperator("duplicateConsole", new ConsoleOutputOperator());

    dag.addStream("serialData", serialNumberGenerator.out, deduper.input);
    dag.addStream("uniqueData", deduper.unique, uniqueconsole.input);
    dag.addStream("duplicateConsole", deduper.duplicate, duplicateConsole.input);
  }
}
